package si.fri.rso.admin.models.converters;


import si.fri.rso.admin.lib.Report;
import si.fri.rso.admin.models.entities.ReportEntity;

import java.util.stream.Collectors;

public class ReportConverter {

    public static Report toDto(ReportEntity entity) {

        Report dto = new Report();
        // Map dto into entity
        dto.setId(entity.getId());
        dto.setOcenaId(entity.getOcenaId());
        dto.setUserId(entity.getUserId());
        dto.setKomentar(entity.getKomentar());
        dto.setTimestamp(entity.getTimestamp());

        return dto;

    }

    public static ReportEntity toEntity(Report dto) {

        ReportEntity entity = new ReportEntity();
        // Map dto into entity
        if (dto.getId() != null) entity.setId(dto.getId());
        entity.setOcenaId(dto.getOcenaId());
        entity.setUserId(dto.getUserId());
        entity.setKomentar(dto.getKomentar());
        entity.setTimestamp(dto.getTimestamp());

        return entity;

    }

}
