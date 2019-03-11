import java.util.ArrayList;
import java.util.Arrays;

public class DemoDig {
    static int trials = 10;

    public static void main(String[] args) {
        String[] top25 = {"Google.com", "Youtube.com", "Facebook.com", "Baidu.com", "Wikipedia.org", "Qq.com",
                "Yahoo.com", "Tmall.com", "Taobao.com", "Amazon.com", "Twitter.com", "Sohu.com", "Jd.com", "Vk.com",
                "Live.com", "Instagram.com", "Yandex.ru", "Weibo.com", "Sina.com.cn", "360.cn", "Reddit.com", "Login.tmall.com",
                "Blogspot.com", "Netflix.com", "Linkedin.com"};
        int[][] times = new int[top25.length][trials];
        DigTimer digTimer = new DigTimer();

        for (int siteIndex = 0; siteIndex < top25.length; siteIndex++){
            String site = top25[siteIndex];
            System.out.println(site);
            digTimer.newQuery(site);
            int sum = 0;
            for (int i = 0; i < trials; i++){
                digTimer.runDig();
                System.out.printf("%d\n", digTimer.getLastTime());
                times[siteIndex][i] = (int)digTimer.getLastTime();
                sum += digTimer.getLastTime();
            }
            int ave = sum / trials;
//            times[siteIndex][trials] = sum / trials;
            int quater = getNPercentile(times[siteIndex], 25);
            int threeQuter = getNPercentile(times[siteIndex], 75);
            System.out.printf("\nAverage: %dms, 25thPercentile:, %d 75thPercentile: %d\n\n", ave, quater, threeQuter);

        }
//        System.out.println("Bonus : ");
//        MyDig diggyboi = new MyDig("google.co.jp");
//        Record answer = diggyboi.run();
//        System.out.println(answer);
    }

    public static int getNPercentile(int[] times, int perctile){
        Arrays.sort(times);
        int answer;
        double index = times.length * (perctile/100.0);
        if(Math.floor(index) == Math.ceil(index)){
            answer = (int)((times[(int)index] + times[(int)index+1]) / 2);
        }else {
            answer = times[(int)Math.round(index)];
        }
        return answer;
    }
}
