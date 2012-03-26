/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import memo.db.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import memo.db.annotation.Transient;

/**
 *
 * @author djr
 */
public class Memo extends Dao<Memo> {
    //

    public final static Memo db = new Memo();
    private Long id;
    private Long parentId;
    private String name;
    private String content;
    @Transient
    private List<Memo> children = null;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取所有根
     * @return
     */
    public static List<Memo> getRoots() {
        List<Memo> list = Memo.db.queryList("select * from memo where parent_id isnull");
        return list;
    }

    /**
     * 获取下级节点
     * @return
     */
    public List<Memo> getChildren() {
        if (children == null) {
            children = Memo.db.queryList("select * from memo where parent_id = ?", this.id);
        }

        return children;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static void main(String[] args) {
//        Dao dao = new Dao();
        Memo m = Memo.db.query("select * from memo");
        System.out.println(m.getName());

        Connection conn = getConn();
        Memo memo1 = new Memo();
        memo1.setId(2L);
        memo1.setName("memo2");
        memo1.setContent("this is memo2");
//        m.insert(conn, memo1);

        Memo.db.update(getConn(),"update memo set parent_id = null where id > ? and id < 50",20L);

    }
}
