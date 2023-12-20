const processVideo = async (file) => {
  console.log("got video", file);
  const MINIMUM_SIZE = 10 * 1024 * 1024; // 50MB

  const size = file.size;
  const totalChunks = calculateTotal(size, MINIMUM_SIZE);

  let start = 0;
  let index = 1;
  let videoId = -1;
  while (start < size) {
    const chunk = file.slice(start, start + MINIMUM_SIZE);
    const meta = getMetaData(chunk, file, index, videoId);
    resp = await uploadChunk(chunk, meta);
    // console.log("resp ", resp);
    videoId = resp.videoId;
    start += MINIMUM_SIZE;
    index += 1;
  }
};

const calculateTotal = (size, min) => {
  return Math.ceil(size / min);
};

const getMetaData = (chunk, file, index, videoId) => {
  return {
    name: file.name,
    index: index,
    type: file.type,
    size: chunk.size,
    total: file.size,
    videoId: videoId,
  };
};

const uploadChunk = async (chunk, meta) => {
  return new Promise((success, failure) => {
    // console.log("chunk", chunk);
    const formData = new FormData();
    formData.append("file", chunk);
    formData.append("details", JSON.stringify(meta));
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/videos", ["async"]);
    xhr.addEventListener("load", function () {
      if (xhr.status >= 200 && xhr.status < 300) {
        try {
          let jsonResponse = JSON.parse(xhr.responseText);
          success({ videoId: jsonResponse.videoId });
          console.log("Response JSON:", jsonResponse);
        } catch (error) {
          console.error("Error parsing JSON:", error);
        }
      } else {
        console.error("Request failed with status:", xhr.status);
      }
    });
    xhr.addEventListener("error", function () {
      console.error("Request failed");
    });

    xhr.send(formData);
  });
};
