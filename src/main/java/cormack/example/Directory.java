package cormack.example;

import java.util.*;

public class Directory {

    private class DirRow {
        public int i;
        public int r;
        public int key;

        public DirRow() {
            i = 0;
            r = 1;
            key = -1;
        }
    }
    private final int _size;
    private final DirRow[] _data;
    private final PrimaryFile _file;

    private final String FOUND_I_ERROR = "error";
    private final String FOUND_I_ERROR_FATAL = "fatal";
    private final String FOUND_I_SUCCESS = "success";

    public Directory(int size) {
        _size = size;
        _file = new PrimaryFile(size);
        _data = new DirRow[size];
    }

    private int _h(int value) {
        return value % _size;
    }

    private int _h(int value, int r, int i) {
        return (value >> i) % r;
    }
    private int _h(int iShiftedValue, int r) { return iShiftedValue % r; }

    private int[] _organizeValues(int r, int[] positions, Integer[] values) {
        int[] newValues = new int[r];
        Arrays.fill(newValues, -1);

        for (int i = 0; i < values.length; i++) {
            newValues[positions[i]] = values[i];
        }

        return newValues;
    }
    private HashSet<Integer> _calcOccupiedPositions() {
        HashSet<Integer> positions = new HashSet<>();
        for (DirRow row : _data) {
            if (row != null) {
                for (int pos = row.key; pos < row.key + row.r; pos++) {
                    positions.add(pos);
                }
            }
        }
        return positions;
    }
    private Pair<String, int[]> _calcPositions(int r, int i, Integer[] values) {
        int[] positions = new int[values.length];
        HashSet<Integer> distinctCount = new HashSet<>();
        int underFlowCounter = 0;

        for (int x = 0; x < values.length; x++) {
            int iShiftedValue = values[x] >> i;
            if (iShiftedValue == 0) {
                underFlowCounter += 1;
            }
            positions[x] = _h(iShiftedValue, r);
            distinctCount.add(positions[x]);
        }

        if (underFlowCounter == values.length) {
            return new Pair<>(FOUND_I_ERROR_FATAL, null);
        } else if (distinctCount.size() < values.length) {
            return new Pair<>(FOUND_I_ERROR, null);
        } else {
            return new Pair<>(FOUND_I_SUCCESS, positions);
        }
    }
    private Pair<Integer, int[]> _findI(int r, Integer[] values) {
        int i = 0;
        Pair<String, int[]> positionsComputation = _calcPositions(r, i, values);
        while (! FOUND_I_SUCCESS.equals(positionsComputation.getFirst())) {
            if (FOUND_I_ERROR.equals(positionsComputation.getFirst())) {
                i += 1;
                positionsComputation = _calcPositions(r, i, values);
            }
            else if (FOUND_I_ERROR_FATAL.equals(positionsComputation.getFirst())){
                return new Pair<>(-1, null);
            }
        }
        return new Pair<>(i, positionsComputation.getSecond());
    }
    private void _updateParams(int row, Integer[] values) {
        int r = values.length;

        Pair<Integer, int[]> params = _findI(r, values);
        while (params.getFirst() == -1) {
            r += 1;
            params = _findI(r, values);
        }
        _file.setOccupiedPositions(_calcOccupiedPositions());
        _data[row].i = params.getFirst();
        _data[row].r = r;
        int[] newValues = _organizeValues(r, params.getSecond(), values);
        _data[row].key = _file.save(newValues);
    }

    private void _save(int row, int value) {
        ArrayList<Integer> values = _file.delete(_data[row].key, _data[row].r);
        values.add(value);
        Integer[] intValues = values.toArray(new Integer[0]);
        _updateParams(row, intValues);
    }

    public void add(int value) {
        int row = _h(value);
        if (_data[row] == null) {
            _data[row] = new DirRow();
            _data[row].i = 0;
            _data[row].r = 1;
            _file.setOccupiedPositions(new HashSet<>());
            _data[row].key = _file.save(value);
        }
        else {
            _save(row, value);
        }
    }

    public void delete(int value) {
        int row = _h(value);
        int pos = _h(value, _data[row].r, _data[row].i);
        int inx = _data[row].key + pos;
        if (_file.get(inx) == value) {
            _file.delete(inx, 1);
        }
    }

    public void printContent() {
        System.out.println("Directory:");
        for (int i = 0; i < _data.length; i++) {
            System.out.print(i);
            if (_data[i] == null) {
                System.out.println("null");
            }
            else {
                System.out.print(", r: ");
                System.out.print(_data[i].r);
                System.out.print(", i: ");
                System.out.print(_data[i].i);
                System.out.print(", ptr: ");
                System.out.println(_data[i].key);
            }
        }

        _file.printContent();
    }
}
