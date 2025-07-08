package gameScore;

public class Score {
    int score=0;
    public Score() {
        init();
    }
    public void init() {
        score=0;
    }
    public void add(int x) {
        score+=x;
    }
    public int getScore() {
        return score;
    }
}
