package cn.mandroid.express.Model.Bean;

/**
 * Created by Administrator on 2016/2/26 0026.
 */
public class FilterBean {
    private boolean pennding;
    private boolean running;
    private boolean finish;
    private String depo;
    private String dest;

    public boolean isPennding() {
        return pennding;
    }

    public void setPennding(boolean pennding) {
        this.pennding = pennding;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getDepo() {
        return depo;
    }

    public void setDepo(String depo) {
        this.depo = depo;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
