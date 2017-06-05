package com.dominikgames.filefinder.filefinder;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Dominik on 04.06.2017.
 */

public class Heap {
    private int size;
    FileWithUrl[] heap = new FileWithUrl[0];
    private int nh = 0;

    public Heap(int size){
        this.size = size;
    }

    private void swap(int x, int y){
        FileWithUrl tmp = heap[x];
        heap[x] = heap[y];
        heap[y] = tmp;
    }

    private void up(int i){
        while (i > 1 && heap[i/2].size > heap[i].size) {
            swap(i/2, i);
            i = i/2;
        }
    }

    private void down(int i) {
        int j;
        while (2*i<=nh) {
            j = 2 * i;
            if (j + 1 <= nh && heap[j+1].size < heap[j].size)
                j++;
            if (heap[i].size<heap[j].size)
                break;
            swap(i, j);
            i = j;
        }
    }

    public void insert(FileWithUrl x) {
        heap[++nh] = x;
        up(nh);
    }


    public int removeMin() {
        if (nh <= 0)
            return 0;
        heap[1] = heap[nh--];
        down(1);
        return 1;
    }

    public void expandArray(){
        heap = Arrays.copyOf(heap, size);
    }
}
