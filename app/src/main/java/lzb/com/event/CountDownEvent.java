package lzb.com.event;

/**
 * Created by Administrator on 2015/8/3.
 */
public class CountDownEvent {

    private int max;

    public CountDownEvent(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
