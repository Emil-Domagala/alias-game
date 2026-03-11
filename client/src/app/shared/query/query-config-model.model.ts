export interface QueryConfig {
  search?: SearchConfig
  sorting: SortConfig
  filters: FilterConfig[]
  pageSizes: number[]
}

export interface SearchConfig {
  fields: string[]
  placeholder?: string
}

export interface SortConfig {
  defaultSort: string
  defaultOrder: "ASC" | "DESC"
  sortFields: SortField[]
}

export interface SortField {
  field: string
  label: string
}

export interface FilterConfig {
  field: string
  type: FilterType
  options?: string[]
  operators: FilterOperator[]
}

export enum FilterOperator {
  EQ = "EQ",
  CONTAINS = "CONTAINS",
  LT = "LT",
  GT = "GT",
  IN = "IN"
}

export enum FilterType {
  TEXT = "TEXT",
  SELECT = "SELECT",
  NUMBER = "NUMBER"
}

export interface QueryFilter {
  field: string
  operator: FilterOperator
  value: string
}
