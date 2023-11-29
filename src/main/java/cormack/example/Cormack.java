package cormack.example;

public class Cormack {

    private final Directory _dir;
    public Cormack(int directorySize) {
        _dir = new Directory(directorySize);
    }

    public void add(int value){
        _dir.add(value);
    }

    public void delete(int value) {
        _dir.delete(value);
    }

    public void printContent() {
        _dir.printContent();
    }
}
