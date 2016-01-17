package by.bsuir.sweider_b.banksystem.shared.services.credits;

import java.io.Serializable;

/**
 * Created by sweid on 17.01.2016.
 */
public class CreditShowObject implements Serializable {
    private int id;
    private String title;
    private String description;
    private int lenght;
    private long min;
    private long max;
    private int count;

    public CreditShowObject(int id, String title, String description, int lenght, long min, long max, int count) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lenght = lenght;
        this.min = min;
        this.max = max;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLenght() {
        return lenght;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }
}
