export default function loadNasaImages() {
  return fetch('https://images-api.nasa.gov/search?media_type=image&q=earth')
    .then((res) => {
      return res.json()
    })
    .then((data) => {
      if (!data || !data.collection || !data.collection.items) return []
      const dataItems = data.collection.items
      const items: IItem[] = []
      for (const dataItem of dataItems) {
        const item = {
          id: dataItem.data[0].nasa_id,
          title: dataItem.data[0].title,
          uri: dataItem.links[0].href,
        }
        items.push(item)
      }
      return items
    })
}

export interface IItem {
  id: string
  title: string
  uri: string
}
