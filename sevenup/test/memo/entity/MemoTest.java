/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memo.entity;

import entity.Memo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author djr
 */
public class MemoTest {

    @Test
    public void test() {
      List<Memo> list = Memo.db.queryList("select * from memo");
      for(Memo m : list) {
          System.out.println(m.getId() + ":" + m + " " + m.getParentId());
      }
    }

    @Test
    public void insert() throws SQLException {
        Long maxId = Memo.db.queryLong("select max(id) from memo ");

        int i = 0;
        for (; i < 0; maxId++,i++) {
            Memo m = new Memo();
            m.setId(maxId);
            m.setName("name" + maxId);
            m.setParentId(1L);
            m.setContent("content" + maxId);
            m.insert();
        }


    }

    @Test
    public void testGetRoots() {
        List<Memo> list = Memo.getRoots();
        for (Memo m : list) {
            System.out.println(m.getName());
        }
    }

     @Test
    public void getChildren() {
        Memo root = new Memo();
        root.setId(1L);
        List<Memo> list =root.getChildren();
        for (Memo m : list) {
            System.out.println(m.getName());
        }
    }
}
