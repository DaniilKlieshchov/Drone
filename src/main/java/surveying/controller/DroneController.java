package surveying.controller;

import surveying.controller.utilities.EntryData;
import surveying.controller.utilities.Parser;
import surveying.drone.Drone;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class DroneController {

    public static final Path PATH = Paths.get("src/main/resources");
    private boolean isCorrect;

    public int mainMenu(final Drone drone) throws IOException, InterruptedException, BrokenBarrierException {
        final Scanner scanner = new Scanner(System.in);
        int chose;
        Operation operation;

        do {
//            if (chose != 0) Main.barrier.await();
            isCorrect = true;
            System.out.println("""
                    \nChoose the option:
                    1 - Choose the route to start survey
                    2 - Check battery level
                    3 - Check drone status
                    4 - Turn off the drone
                    5 - Pause drone
                    6 - Reset drone
                    7 - Abort mission
                    8 - Check progress
                    9 - Get location""");
            System.out.println();

            chose = scanner.nextInt();

            if (chose < 1 || chose > 9) {
                isCorrect = false;
                System.out.println("\nEnter correct number of option.\n");
                chose = 0;
            }


            operation = Operation.fromId(chose);
            drone.setOperation(Operation.fromId(chose));
            Thread.sleep(200);
            if (Objects.requireNonNull(operation) == Operation.ROUTE) {
                startSurvey(drone);
//                case BATTERY -> System.out.println("The battery level: " + (int) drone.getBattery() + "%");
//                case STATUS -> System.out.println("The drone status: " + drone.status);
//                case EXIT -> System.out.println("Good bye");
            }
//            if (Objects.requireNonNull(operation) == Operation.PAUSE) {
//                Main.barrier.await();
//            }

        } while (!isCorrect);
        return chose;
    }

    public String chooseRoute() throws IOException {
        final Scanner scanner = new Scanner(System.in);

        String fileName;
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

    public static boolean isFileExists(final File file) {
        return file.exists() && !file.isDirectory();
    }

    public void startSurvey(final Drone drone) throws IOException, InterruptedException {
        final Parser parser = new Parser("src/main/resources/" + chooseRoute());
        final List<EntryData> entryData = parser.parseFile();
        drone.clearData();
        drone.recordInput(entryData);
        new Thread(() -> {
            try {
                drone.survey(entryData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
