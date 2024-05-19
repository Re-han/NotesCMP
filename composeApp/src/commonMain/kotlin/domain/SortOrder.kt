package domain

sealed class SortOrder() {
    data object Sort_By_Asc : SortOrder()
    data object Sort_By_Desc : SortOrder()
    data object Sort_By_Date_Latest : SortOrder()
    data object Sort_By_Date_Oldest : SortOrder()
}
