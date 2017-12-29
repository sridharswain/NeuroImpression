module.exports = function(link){
  const prev = "<html> <head><meta name=\"viewport\" content=\"width=device-width\"></head><body><video controls=\"\" autoplay=\"\" name=\"media\"><source src=\"";
  const next = "\" type=\"video/webm\"></video></body></html>";

  return prev+link+next;
}
