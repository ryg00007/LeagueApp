package stir.ac.uk.leaguestatsapp;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChampionList.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChampionListDao championListDao();
}
