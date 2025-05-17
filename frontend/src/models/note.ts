export interface NoteDTO {
  id: number
  name: string
  folderId: number | null
  creation: Date
  lastUpdate: Date
}

export interface NoteDataDTO {
  id: number
  content: string
}

export interface NoteCategoryDTO {
  id: number
  name: string
  lastUpdate: Date
  color: string
}

export interface FolderDTO {
  id: number
  name: string
  folders: FolderDTO[]
  notes: NoteDTO[]
  categoryId: number | null
  sessionId: number | null
  creation: Date
  lastUpdate: Date
}

export interface NoteTreeKey {
  type: 'FOLDER' | 'NOTE'
  id: number
}

export interface NoteTreeDTO {
  key: NoteTreeKey
  name: string
  children: NoteTreeDTO[]
}
