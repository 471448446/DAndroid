package better.learn.eventbus.event;

/**
 * Created by better on 2018/4/2 14:14.
 */

public class MessageEvent {
    int messageType;

    public MessageEvent() {
    }

    public MessageEvent(int messageType) {
        this.messageType = messageType;
    }
}
