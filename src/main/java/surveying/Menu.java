package surveying;

import surveying.drone.Drone;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Menu {

    public static final Path PATH = Paths.get("src/main/resources");
    private boolean isCorrect;
    private String fileName;
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

    public int getBatteryLevel(Drone drone) {
        return (int)drone.getBattery();
    }
}
