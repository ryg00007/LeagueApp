package stir.ac.uk.leaguestatsapp;



import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ChampionListDao {
    @Query("SELECT * FROM ChampionList")
    List<ChampionList> getAll();

    @Query("SELECT * FROM ChampionList WHERE uid = :ChampID")
    List<ChampionList> loadById(int ChampID);

    @Query("SELECT uid FROm ChampionList WHERE Name = :ChampName")
    List<ChampionList> loadByName(String ChampName);
}
