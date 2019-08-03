
export function testUrl(w, h, text) {
  const sizeText = `${w}x${h}`
  return `https://via.placeholder.com/${sizeText}.png?text=${text ? text : sizeText}`
}