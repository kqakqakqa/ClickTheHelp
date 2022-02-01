package cc.mcii.Helper;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class HelperBean implements ConfigurationSerializable {
    private Integer type;
    private String content;
    private String lore;
    private String text;

    public HelperBean(Map<?,?> map) {
        if(map.get("type") != null) {
            this.type = Integer.valueOf(map.get("type").toString());
        } else {
            this.type = 0;
        }
        this.content = map.get("content").toString();
        this.lore = map.get("lore").toString();
        this.text = map.get("text").toString();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", this.type);
        map.put("content", this.content);
        map.put("lore", this.lore);
        map.put("text", this.text);
        return map;
    }
}
