function enterEvent() {
    document.addEventListener("keypress", function (event) {
        console.log(event);
        if (event.keyCode === 13) { // 13 for enter key
          send();
        }
      });
}

// module.exports = {
//     enterKey
// }