package com.simbirsoft.farm.databind;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class LocalDateTimeImplicitContructor extends Constructor {

    public LocalDateTimeImplicitContructor() {
        this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructLocalDateTime());
    }
    
    private class ConstructLocalDateTime extends ConstructYamlTimestamp {
        @Override
        public Object construct(Node node) {
            Date date = (Date) super.construct(node);
            Instant instant = Instant.ofEpochMilli(date.getTime());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }
}
