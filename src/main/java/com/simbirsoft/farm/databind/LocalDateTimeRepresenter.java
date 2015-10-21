package com.simbirsoft.farm.databind;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

class LocalDateTimeRepresenter extends Representer {

    public LocalDateTimeRepresenter() {
        multiRepresenters.put(LocalDateTime.class, new RepresentLocalDateTime());
    }

    private class RepresentLocalDateTime extends RepresentDate {

        @Override
        public Node representData(Object data) {
            LocalDateTime date = (LocalDateTime) data;
            return super.representData(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
        }
    }
}
