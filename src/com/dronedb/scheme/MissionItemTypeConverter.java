package com.dronedb.scheme;

import javax.persistence.AttributeConverter;

public class MissionItemTypeConverter implements AttributeConverter<MissionItemType, Integer>
{
    @Override
    public Integer convertToDatabaseColumn(MissionItemType missionItemType) {
        return missionItemType.getOrder();
    }
 
    @Override
    public MissionItemType convertToEntityAttribute(Integer dbValue) {
        // this can still return null unless it throws IllegalArgumentException
        // which would be in line with enums static valueOf method
        return MissionItemType.fromDbValue(dbValue);
    }
}
