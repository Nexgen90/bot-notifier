package ru.nexgen.botnotifier.services.map;

import lombok.AllArgsConstructor;

/**
 * Created by nikolay.mikutskiy
 * Date: 02.09.2020
 */
@AllArgsConstructor
public enum MapType {
    SAFE_ZONE ("Безопасная зона"),
    FOREST ("Лес"),
    CITY ("Город"),
    MOUNTAINS ("Горы"),
    FLATLAND ("Равнина"),
    UNDERGROUND ("Подземелье");

    String name;
}
