package cormack.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PrimaryFile {
    int[] _data;
    HashSet<Integer> _occupiedPositions;

    public PrimaryFile(int size) {
        _data = new int[size];
        Arrays.fill(_data, -1);
    }

    public void printContent() {
        System.out.println("Primary File:");
        for(int i = 0; i < _data.length; i++) {
            System.out.print("  ");
            System.out.print(i);
            System.out.print(": ");
            System.out.println(_data[i]);
        }
        System.out.println();
    }
    public void setOccupiedPositions(HashSet<Integer> positions) {
        _occupiedPositions = positions;
    }
    private void _resizeData() {
        int[] newData = new int[_data.length * 2];
        Arrays.fill(newData, -1);

        for(int i = 0; i < _data.length; i++) {
            newData[i] = _data[i];
        }

        _data = newData;
    }

    private boolean _isEmpty(int pos) {
        if (pos < _data.length) {
            return _data[pos] == -1 && ! _occupiedPositions.contains(pos);
        }
        return false;
    }

    private boolean _isEmptyWindow(int pos, int size) {
        boolean result = true;
        for(int i = 0; i < size; i++) {
            result = result && _isEmpty(i + pos);
        }
        return result;
    }

    public int findPlace(int size) {
        int fstInx = 0;

        while(! _isEmptyWindow(fstInx, size)) {
            fstInx += 1;
            if (fstInx + size - 1 > _data.length) {
                _resizeData();
                fstInx = 0;
            }
        }
        return fstInx;
    }

    public int save(int value) {
        int inx = findPlace(1);
        _data[inx] = value;
        return inx;
    }
    public int save(int[] values) {
        int inx = findPlace(values.length);
        for (int i = 0; i < values.length; i++) {
            _data[inx + i] = values[i];
        }
        return inx;
    }

    public ArrayList<Integer> delete(int key, int size) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        if (key + size - 1 < _data.length) {
            for(int i = 0; i + key < key + size; i++) {
                if (_data[i + key] != -1) {
                    values.add(_data[i + key]);
                }
                _data[i + key] = -1;
            }
        }
        else {
            System.err.println("Error: Delete out of index");
        }
        return values;
    }

    public int get(int key) {
        return _data[key];
    }
}
