package Kol1.FileSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.Scanner;

interface IFile{
    String getFileName();
    long getFileSize();
    String getFileInfo();
    void sortBySize();
    long findLargestFile();
}

class File implements IFile{
    private String name;
    private long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo() {
        return String.format("File name: %10s File size: %10d\n", name, size);
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class Folder implements IFile{
    private String name;
    private long size;
    private List<IFile> list;

    public Folder(String name) {
        this.name = name;
        list = new ArrayList<IFile>();
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        for(int i=0; i<list.size(); i++){
            size += list.get(i).getFileSize();
        }

        return size;
    }

    @Override
    public String getFileInfo() {
        return String.format("Folder name: %10s Folder size: %10d\n", name, size);
    }

    @Override
    public void sortBySize() {
//        list.stream().sorted();
    }

    @Override
    public long findLargestFile() {
        OptionalLong ol = list.stream().mapToLong(IFile::findLargestFile).max();

        if (ol.isPresent())
            return ol.getAsLong();
        return 0L;
    }

    void addFile (IFile file) throws FileNameExistsException {
        if (list.contains(file))
            throw new FileNameExistsException(file.getFileName(), name);
        else {
            list.add(file);
        }
    }
}

class FileSystem{
    private final Folder folder;

    public FileSystem() {
        folder = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        folder.addFile(file);
    }

    public void sortBySize() {
//        folder.sortBySize();
    }

    public long findLargestFile() {
        return folder.findLargestFile();
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}