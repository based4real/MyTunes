package mytunes.BE.REST;

import java.util.Objects;

public class Release {
    private String releaseID, releaseGroupID, title, date;
    private boolean isSingle;
    int songPos;

    public Release(String releaseID, String releaseGroupID, String title, String date, boolean isSingle, int songPos) {
        this.releaseID = releaseID;
        this.releaseGroupID = releaseGroupID;
        this.title = title;
        this.date = date;
        this.isSingle = isSingle;
        this.songPos = songPos;
    }

    public String getReleaseId() {
        return releaseID;
    }

    public String getReleaseGroupId() {
        return releaseGroupID;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsSingle() {
        return isSingle;
    }

    public String getDate() {
        return date;
    }

    public int getSongPos() {
        return songPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Release release = (Release) o;
        return Objects.equals(title, release.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
