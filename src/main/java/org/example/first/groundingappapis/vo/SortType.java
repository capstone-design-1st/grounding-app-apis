package org.example.first.groundingappapis.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.first.groundingappapis.exception.PropertyErrorResult;
import org.example.first.groundingappapis.exception.PropertyException;

@Getter
@RequiredArgsConstructor
public enum SortType {
    LIKES("likes"),
    VIEWS("views"),
    VOLUMES("volumes"),
    DEFAULT("default");

    private final String sortType;

    public static String getSortField(SortType sortType) {
        switch (sortType) {
            case LIKES:
                return "likeCount";
            case VIEWS:
                return "viewCount";
            case VOLUMES:
                return "volumeCount";
            default:
                return "createdAt";
        }
    }

    public static SortType verifyAndConvertStringToSortType(String sort) {
        if (sort.equals(LIKES.getSortType())) {
            return SortType.LIKES;
        }
        if (sort.equals(VIEWS.getSortType())) {
            return SortType.VIEWS;
        }
        if (sort.equals(VOLUMES.getSortType())) {
            return SortType.VOLUMES;
        }
        if (sort.equals(DEFAULT.getSortType())) {
            return SortType.DEFAULT;
        }
        throw new PropertyException(PropertyErrorResult.INVALID_SORT_TYPE);
    }
}
