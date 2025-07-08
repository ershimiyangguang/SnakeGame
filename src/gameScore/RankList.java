package gameScore;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RankList {
    int[] history;//历史成绩数组
    int score;//此次成绩
    int ranking;//此次成绩对应排名

    public RankList(int score) throws IOException {
        //存储传入的成绩
        this.score = score;
        //读取scores.txt，获得历史成绩
        List<Integer> integers = readTXT("Resource/Save/scores.txt");
//        System.out.println(integers);
        //降序排序history与score并存入history
        integers.add(score);
        Collections.sort(integers, Collections.reverseOrder());
        history = integers.stream().mapToInt(Integer::intValue).toArray();
        //将数组s写入scores.txt
        saveXT(history,"Resource/Save/scores.txt");
    }
    public static List<Integer> readTXT(String filePath) throws IOException {
        List<Integer> scores = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            scores.add(Integer.parseInt(line.trim()));
        }
        reader.close();
        return scores;
    }
    public static void saveXT(int[] s, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (int score : s) {
            writer.write(score + "\n");
        }
        writer.close();
    }
    public int getScore(int x) {
//        获取排名x的成绩
        return history[x-1];
    }

    public int getRanking() {
        //获取此次成绩的排名
        //bs需升序排序再使用
        int[] orders = new int[history.length];
        System.arraycopy(history,0, orders,0, history.length);
        Arrays.sort(orders);
        ranking =  history.length - Arrays.binarySearch(orders, score);
        return ranking;
    }
    public void clear() throws IOException{
        int[] a = {-999999};
        history = a;
        //将数组s写入scores.txt
        saveXT(history,"Resource/Save/scores.txt");
    }
}
