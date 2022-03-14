package commons;

import javax.persistence.Entity;
import java.util.List;
import java.util.Objects;

@Entity
public class Scores{

    public String username;
    public int score;

    public Scores(String username, int score) {
        this.username;
        this.score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scores scores = (Scores) o;
        return score == scores.score && Objects.equals(username, scores.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, score);
    }

    @Override
    public String toString() {
        return "Scores{" +
                "username='" + username + '\'' +
                ", score=" + score +
                '}';
    }
}
