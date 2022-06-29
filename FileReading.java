import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReading {
    static final String RESULT_FILE = "ResultFile.txt";
    static final String START_TEXT = "Программа соберет все текстовые файлы в указанной директории с учетом вложений";
    static final String END_TEXT = "Программа завершена. Создан файл: ";
    static final String ENTER_PATH = "Введите путь к директории:";
    static final String REPEATE_ENTRANCE = "Введен некорректный путь к файлу. Повторите ввод";

    public static void main(String[] args) {
        System.out.println(START_TEXT);
        String path = getPath();
        List<File> files = getAllTextFiles(path);

        while (files == null) {
            System.out.println(REPEATE_ENTRANCE);
            path = getPath();
            files = getAllTextFiles(path);
        }

        if (!files.isEmpty()) {
            File newFile = createNewFile(path, RESULT_FILE);
            fillNewFile(newFile, files);
        }
        System.out.println(END_TEXT + path + File.separator + RESULT_FILE);
    }

    //решение требует изменения, для повышения производительности, если файлы ожидаются слишком тяжелыми или слишком лёгкими.
    //в данном случае усредненное оптимальное решение
    static void fillNewFile(File resulFile, List<File> filesToRead) {
        try (BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resulFile, false), StandardCharsets.UTF_8))) {
            for (File file : filesToRead) {
                String lineToWrite = readFile(file);
                bwr.write(lineToWrite);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder("***** " + file.getName().toUpperCase() + " *****\n");
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString().trim() + "\n";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    static String getPath() {
        try {
            String path = "";
            while (path == null || path.equals("")) {
                System.out.println(ENTER_PATH);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                path = br.readLine();
            }
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<File> getAllTextFiles(String path) {
        try (Stream<Path> filePathStream = Files.walk(Paths.get(path))) {
            return filePathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".txt"))
                    .filter(file -> !file.getName().equals(RESULT_FILE))
                    .sorted(Comparator.comparing(File::getName))
                    .collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    static File createNewFile(String path, String fileName) {
        try {
            String resultFile = path + File.separator + fileName;
            File f = new File(resultFile);
            if (!f.exists()) {
                f.createNewFile();
            }
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Path - " + path);
            System.out.println("FileName - " + fileName);
            return null;
        }
    }
}
