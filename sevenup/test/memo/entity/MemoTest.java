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
    public void testQuery() {
        Memo m = Memo.db.query("select * from memo where id=2");
        Assert.assertNotNull(m);
        m.setParentId(1L);
        Memo.db.update(Memo.getConn(), m);
        m = Memo.db.query("select * from memo where id=2");
        System.out.println(m.getParentId());
    }

    @Test
    public void insert() throws SQLException {
        Long maxId = Memo.db.queryLong("select max(id) from memo ");
        Connection conn = Memo.getConn();
        conn.setAutoCommit(false);
        int i = 0;
        for (; i < 10; maxId++,i++) {
            Memo m = new Memo();
            m.setId(maxId);
            m.setName("name" + maxId);
            m.setParentId(1L);
            m.setContent("content" + maxId);
            Memo.db.insert(conn, m);
        }
//        Memo.commitAndClose(conn);

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
