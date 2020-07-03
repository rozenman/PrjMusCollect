package ru.mycompany.audio_metadata;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import ru.mycompany.model.CollectionItem;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Decode_audio {

    public static void Decode_audio(){
        //Disable loggers
        Logger[] pin = new Logger[]{ Logger.getLogger("org.jaudiotagger") };

        for (Logger l : pin)
            l.setLevel(Level.OFF);
    }

    public static CollectionItem decode_info(String fname) {
        CollectionItem item  = new CollectionItem();
        //item.setDuration("0");
        //Disable loggers
        Logger[] pin = new Logger[]{ Logger.getLogger("org.jaudiotagger") };

        for (Logger l : pin)
            l.setLevel(Level.OFF);
        try {
            File file = new File(fname);
            //FlacFileReader

            //MP3File f = new MP3File(file);
            //System.out.println("@@@@@@@@@@@"+file);
            AudioFile f = AudioFileIO.read(file);
            //System.out.println("@@@@@@@@@@@"+f);
            Tag tag = f.getTag();
            AudioHeader audioHeader = f.getAudioHeader();
            //System.out.println("###########"+audioHeader);
            int TrackLength = audioHeader.getTrackLength();
            System.out.println("TrackLength:: "+ TrackLength + ":" + item);
            //ID3v24Tag v24tag = (ID3v24Tag) f.getID3v2TagAsv24();


            if (tag == null ) return item;
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            String title = tag.getFirst(FieldKey.TITLE);
            String genre = tag.getFirst(FieldKey.GENRE);
            String comment = tag.getFirst(FieldKey.COMMENT);
            String year = tag.getFirst(FieldKey.YEAR);
            String track = tag.getFirst(FieldKey.TRACK);
            String disk_no = tag.getFirst(FieldKey.DISC_NO);
            String composer = tag.getFirst(FieldKey.COMPOSER);
            String artist_sort = tag.getFirst(FieldKey.ARTIST_SORT);
            //String track_total = tag.getFirst(FieldKey.TRACK_TOTAL);
/*
            System.out.println("Duration: "+ TrackLength + ", \nArtist: " + artist + ", \nAlbum: " + album +
                    ", \nTitle: " + title + ", \nGENRE:" + genre +", \nComment: " + comment + ", \nYEAR:" + year + ", \nTrack: " + track +
                    ", \nDOSK_NO: " + disk_no + ", \nCOMPOSER: " + composer + ", \nARTIST_SORT: " + artist_sort);

 */
            item  = new CollectionItem(-1,title, artist, composer, genre, album, "" + TrackLength, file.getParent(), file.getName() );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
            System.out.println("#1");
        } catch (TagException e) {
            e.printStackTrace();
            System.out.println("#2");
        } catch (InvalidAudioFrameException e) {
            //e.printStackTrace();
            System.out.println("#3"+ e);
        } catch (CannotReadException e) {
            e.printStackTrace();
            System.out.println("#4");
        }
        return item;
    }
}
