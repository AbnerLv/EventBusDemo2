package lzb.com.event;

/**
 * Created by Administrator on 2015/8/3.
 */
public class SecondActivityEvent {

    private String text;

    public SecondActivityEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
