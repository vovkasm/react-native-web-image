export function testUrl(w: number, h: number, text?: string) {
  const sizeText = `${w}x${h}`
  return `https://via.placeholder.com/${sizeText}.png?text=${text ? text : sizeText}`
}
