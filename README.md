# MyTunes
Third project on the 1. semester of Computer Science. We had 3 weeks to finish this project, I worked together with one other in making of this project.\
We had to make a music player, where you could import songs and make playlists.

## Usage
Rename the config.example.settings in the folder config to "config.settings" and fill in your data.

## Functionality
The project relies on grabbing music data using MusicBrainz and CoverArt for grabbing pictures.
 - [MusicBrainz API](https://musicbrainz.org/doc/MusicBrainz_API)
 - [CoverArt API](https://musicbrainz.org/doc/Cover_Art_Archive/API)

### Features
- Beautiful Spotify alike look. (FXML and CSS)
- Connection to MusicBrainz and CoverArt REST API's.
- Playing imported song media.
- Automatic and manual search for song metadata, when importing.
- Cache/download of images for faster load.
- Database connection for storage.
- Full CRUD on playlist system, featuring drag and drop.
- Right click on each element in tableview for options.
- Clickable labels in tableview: Artist name, Album and Category.

### Rest API MusicBrainz

- Import multiple albums for one songs, by picking from most releases search.
- Finding artist and featuring on song, both including getting alias.
Picking song with most releases for best search result hit.
``` java
private JSONObject pickMostReleases(JSONArray data) throws JSONException {
    int maxReleases = -1;
    JSONObject jsonBestResult = null;
    String maxTitle = "";

    for (int i = 0; i < data.length(); i++) {
        JSONObject recording = data.getJSONObject(i);
        String title = recording.getString("title");

        // Count releases for the recording
        int releasesCount = 0;
        if (recording.has("releases")) {
            releasesCount = recording.getJSONArray("releases").length();
            if (releasesCount > maxReleases) {
                jsonBestResult = recording;
                maxTitle = title;
                maxReleases = releasesCount;
            }
        }
    }
    return jsonBestResult;
}
```

### Rest API CoverArt
- Importing Front, back and thumbnails from CoverArt API.
```java
public String getFrontCover() throws Exception {
    if (hasFront())
        if (object.has("image"))
            frontImage = object.getString("image");

    return frontImage;
}
```

## Libraries

- fasterxml.jackmson.datatype.json - com.fasterxml.jackson:jackson-datatype-json-org:1.8.0
- google.code.gson - com.google.code.gson:gson:2.10.1
- microsoft.sqlserver.mssql.jdbc - com.microsoft.sqlserver:mssql-jdbc:12.3.1.jre11-preview
- controlsfx - org.controlsfx:controlsfx:11.2.0

## Screenshots
### Search
<img src="https://i.imgur.com/j258a91.png" alt="logo" width="800"/>

### Playlists
<img src="https://i.imgur.com/ENUhFIJ.png" alt="logo" width="800"/>
<img src="https://i.imgur.com/hHQPluZ.png" alt="logo" width="800"/>

### Album
<img src="https://i.imgur.com/ZJ0A2Ym.png" alt="logo" width="800"/>

### Artist
<img src="https://i.imgur.com/FygNjIT.png" alt="logo" width="800"/>

### Import song
<img src="https://i.imgur.com/ev2RBqc.png" alt="logo" height="400"/>

## Database design
MSSQL database diagram

![db](https://i.imgur.com/mqrRMXH.png)

## UML diagram
IntelliJ generated UML diagram
![uml](https://i.imgur.com/ZyrgO4T.png)
