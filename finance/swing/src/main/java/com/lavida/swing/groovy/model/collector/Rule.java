package com.lavida.swing.groovy.model.collector;

//import com.tangosol.io.pof.PofReader;
//import com.tangosol.io.pof.PofWriter;
//import com.tangosol.io.pof.PortableObject;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Rule implements Serializable
//        , PortableObject
{

	private static final long serialVersionUID = -415338459514965045L;

	@Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private RuleType type;

    @ElementCollection
    @CollectionTable(name = "PARAMS")
    @MapKeyColumn(name = "PARAM_KEY")
    @Column(name = "PARAM_VALUE")
    private Map<String, String> params = new HashMap<String, String>();

    public Rule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RuleType getType() {
        return type;
    }

    public void setType(RuleType type) {
        this.type = type;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (id != null ? !id.equals(rule.id) : rule.id != null) return false;
        if (params != null ? !params.equals(rule.params) : rule.params != null) return false;
        if (type != rule.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

/*
    public void readExternal(PofReader pofReader) throws IOException {
        setId(pofReader.readLong(0));
        setType((RuleType) pofReader.readObject(1));
        setParams(pofReader.readMap(2, new HashMap<String, String>()));
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeLong(0, getId());
        pofWriter.writeObject(1, getType());
        pofWriter.writeMap(2, getParams());
    }
*/

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", type=" + type +
                ", params=" + params +
                '}';
    }
}
