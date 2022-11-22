package surveying.menu;

import surveying.EntryData;
import surveying.Parser;
import surveying.drone.Drone;
import surveying.drone.DroneStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu {

    public static final Path PATH = Paths.get("src/main/resources");
    private boolean isCorrect;
    private String fileName;

    public int mainMenu(Drone drone) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int chose = 0;
        Operation operation;

        do {
            isCorrect = true;
            System.out.println("""
                    \nChoose the option:
                    1 - Choose the route to start survey
                    2 - Check battery level
                    3 - Check drone status
                    4 - Turn off the drone""");
            System.out.println();

            chose = scanner.nextInt();

            if (chose < 1 || chose > 4) {
                isCorrect = false;
                System.out.println("\nEnter correct number of option.\n");
                chose = 0;
            }

            operation = Operation.fromId(chose);


            switch (Objects.requireNonNull(operation)) {
                case ROUTE -> startSurvey(drone);
                case BATTERY -> System.out.println("The battery level: " + (int) drone.getBattery() + "%");
                case STATUS -> System.out.println("The drone status: " + drone.status);
                case EXIT -> System.out.println("Good bye");
            }
        } while (!isCorrect);
        return chose;
    }

    public String chooseRoute() throws IOException {
        Scanner scanner = new Scanner(System.in);

        do {
            isCorrect = true;
            System.out.println("Choose the route:\n");

            Files.find(PATH, 1, (p, a) -> a.isRegularFile() && p.getFileName().toString().endsWith(".txt"))
                    .forEach(p -> System.out.println(p.getFileName()));

            System.out.println();

            fileName = scanner.nextLine();

            if (!isFileExists(new File(PATH + "/" + fileName))) {
                isCorrect = false;
                System.out.println("\nEnter correct file name.\n");
            }
        } while (!isCorrect);

        return fileName;
    }

    public static boolean isFileExists(File file) {
        return file.exists() && !file.isDirectory();
    }

    public void startSurvey(Drone drone) throws IOException, InterruptedException {
        Parser parser = new Parser("src/main/resources/" + chooseRoute());
        List<EntryData> entryData = parser.parseFile();
        drone.recordInput(entryData);
        drone.survey(entryData);
    }
}
