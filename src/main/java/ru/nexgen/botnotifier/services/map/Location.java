package ru.nexgen.botnotifier.services.map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by nikolay.mikutskiy
 * Date: 31.08.2020
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Location {
    private Integer id;
    private String code;
    private String name;
    private String smallMapImageId;
    private String fullMapImageId;

    private List<Integer> nextLocationsId;


}
