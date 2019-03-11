import org.xbill.DNS.Record;

public class DigTimer {

    long start;
    long stop;
    long runTime;

    MyDig digger;
    public DigTimer(){
        digger = null;
        runTime = -1;
    }

    public void newQuery(String inquery){
        digger = new MyDig(inquery);
        start = 0;
        stop = 0;
        runTime = -1;
    }

    public Record runDig(){
        start = System.currentTimeMillis();
        Record rec = digger.run();
        stop = System.currentTimeMillis();
        runTime = stop - start;
        return rec;
    }

    public long getLastTime(){
        return runTime;
    }


}
