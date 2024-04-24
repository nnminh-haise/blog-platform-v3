const editor = new EditorJS({
  holder: "editor",
  onReady: () => {
    console.log("Editor is ready");
  },
  tools: {
    header: {
      class: Header,
      config: {
        placeholder: "Enter a header",
        levels: [2, 3, 4],
        defaultLevel: 3,
      },
    },
    list: {
      class: List,
      inlineToolbar: true,
    },
    paragraph: {
      class: Paragraph,
      config: {
        placeholder: "Click here to start typing",
      },
      inlineToolbar: true,
    },
    image: {
      class: ImageTool,
      config: {
        endpoints: {
          byFile: "https://your-backend.com/uploadFile",
          byUrl: "https://your-backend.com/fetchUrl",
        },
      },
    },
    embed: {
      class: Embed,
      inlineToolbar: true,
    },
    link: {
      class: LinkTool,
      config: {
        endpoint: "https://your-backend.com/fetchUrl",
      },
    },
    delimiter: Delimiter,
    code: {
      class: CodeTool,
      inlineToolbar: true,
    },
  },
  inlineToolbar: true,
  toolbar: {
    buttons: [
      "header",
      "bold",
      "italic",
      "link",
      "unorderedList",
      "orderedList",
      "image",
      "embed",
      "code",
    ],
  },
});

console.log("editor", editor);

function saveData() {
  console.log("saved!");
  editor
      .save()
      .then((data) => {
        console.log(data);
        const jsonData = JSON.stringify(data);
        document.getElementById("description").value = jsonData;
        document.getElementById("main-container").submit();
      })
      .catch((error) => {
        console.log("error: ", error);
      });
}
