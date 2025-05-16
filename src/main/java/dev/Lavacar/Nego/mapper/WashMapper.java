package dev.Lavacar.Nego.mapper;

import dev.Lavacar.Nego.model.Wash;
import dev.Lavacar.Nego.dto.WashDTO;
import org.springframework.stereotype.Component;

@Component
public class WashMapper {

    public Wash toEntity(WashDTO dto) {
        if (dto == null) return null;

        Wash wash = new Wash();
        wash.setId(dto.getId());
        wash.setClient(dto.getClient());
        wash.setDate(dto.getDate());
        wash.setValueWash(dto.getValueWash());
        wash.setDescription(dto.getDescription());
        return wash;
    }

    public WashDTO toDTO(Wash wash) {
        if (wash == null) return null;

        return new WashDTO(
                wash.getId(),
                wash.getClient(),
                wash.getDate(),
                wash.getValueWash(),
                wash.getDescription()
        );
    }
}
