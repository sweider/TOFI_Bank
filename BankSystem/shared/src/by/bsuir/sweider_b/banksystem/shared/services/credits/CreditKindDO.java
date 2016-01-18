package by.bsuir.sweider_b.banksystem.shared.services.credits;

import java.io.Serializable;

/**
 * Created by sweid on 17.01.2016.
 */
public class CreditKindDO implements Serializable {
    private int id;
    private String title;
    private String description;
    private int lenght;
    private long min;
    private long max;
    private int count;
    private int percents;

    public CreditKindDO(int id, String title, String description, int length, long min, long max, int percents, int count) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lenght = length;
        this.min = min;
        this.max = max;
        this.count = count;
        this.percents = percents;
    }

    public int getPercents() {
        return percents;
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

    public void setDescription(String desc){
        this.description = desc;
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

    public int getPercent() {
        return 0;
    }
}
