package ca.tnoah.frc.scouting.services.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.tnoah.frc.scouting.models.dbo.Template;

@Dao
public interface TemplatesDAO {

    @Query("SELECT * FROM templates ORDER BY `defaultTemplate` DESC, `version` DESC, `name` ASC")
    List<Template> getAll();

    @Query("SELECT * FROM templates WHERE `id` = :id AND `version` = :version")
    Template get(String id, int version);

    @Query("SELECT * FROM templates WHERE `id` = :id ORDER BY `version` DESC")
    Template getLatest(String id);

    @Query("SELECT * FROM templates WHERE `defaultTemplate` = 1 AND `type` = :type ORDER BY `version` DESC ")
    Template getDefault(String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Template template);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Template> templates);

    @Delete
    void delete(Template template);

}
