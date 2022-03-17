package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Scores{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String username;
    public int score;

    public Scores(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return this.username;
    }

    public int getScore() {
        return this.score;
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
