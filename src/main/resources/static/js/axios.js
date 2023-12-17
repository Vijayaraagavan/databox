function post(url, body, headers) {
  return new Promise((success, failure) => {
    const _headers = {
      "Content-Type": "application/json",
      Authorization: localStorage.getItem("token"),
    };
    if (headers) {
      Object.assign(_headers, headers);
    }
    fetch(url, {
      method: "POST",
      body: JSON.stringify(body),
      headers: _headers,
    })
      .then((resp) => {
        console.log(resp);
        if (resp.status == 401) {
          window.location.href = "/login";
          // failure(resp);
          return;
        }
        // console.log(JSON.parse(resp));
        success(resp);
      })
      .catch((fail) => failure(fail));
  });
}
function httpDelete(url, body, headers) {
  return new Promise((success, failure) => {
    const _headers = {
      "Content-Type": "application/json",
      Authorization: localStorage.getItem("token"),
    };
    if (headers) {
      Object.assign(_headers, headers);
    }
    fetch(url, {
      method: "DELETE",
      body: JSON.stringify(body),
      headers: _headers,
    })
      .then((resp) => {
        console.log(resp);
        if (resp.status == 401) {
          window.location.href = "/login";
          // failure(resp);
          return;
        }
        // console.log(JSON.parse(resp));
        success(resp);
      })
      .catch((fail) => failure(fail));
  });
}
