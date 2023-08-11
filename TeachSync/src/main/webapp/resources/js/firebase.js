/* Trước khi dùng cần thẻ script
*  <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js"></script>
*  <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-storage.js"></script>
* */

/* -------------------------------------------------- Firebase -------------------------------------------------- */
// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCNCbyj7W-rquxcfCuqm4boMEpVXT7wq2k",
    authDomain: "teachsync.firebaseapp.com",
    projectId: "teachsync",
    storageBucket: "teachsync.appspot.com",
    messagingSenderId: "490835666546",
    appId: "1:490835666546:web:de45b02a8d4632ff3f04eb",
    measurementId: "G-WG3VPSFL27"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);


// Initialize Cloud Storage and get a reference to the storage service, which is used to create references in your storage bucket
const storage = firebase.storage();

// Create a storage reference from our storage service
const storageRef = storage.ref();
/* -------------------------------------------------- Firebase -------------------------------------------------- */

/** For upload single file type <b>image/*</b> to Firebase
 * @param imageFile the IMAGE file to upload
 * @returns {Promise<string>}
 */
async function uploadImageFileToFirebaseAndGetURL(imageFile) {
    // Nếu ko có file, ảnh mặc định;
    let imgURL = "../../../resources/img/no-img.jpg";

    // Nếu có file, upload to firebase;
    if (imageFile !== undefined) {
        // Set name, add date to avoid duplicate name;
        let name = (+new Date()) + '-' + imageFile.name;

        // Create file metadata including the content type
        const metadata = {
            contentType: imageFile.type,
            fileSize: imageFile.size
        };

        // Upload the file and metadata
        let task = storageRef.child(name).put(imageFile, metadata);

        let snapshot = await task;

        imgURL = await snapshot.ref.getDownloadURL();
    }

    return imgURL;
}

/** For upload single file type to Firebase
 * @param file the IMAGE file to upload
 * @returns {Promise<string>}
 */
async function uploadFileToFirebaseAndGetURL(file) {
    let url = null;

    // Nếu có file, upload to firebase;
    if (file !== undefined) {
        let name = (+new Date()) + '-' + file.name;

        // Create file metadata including the content type
        const metadata = {
            contentType: file.type,
            fileSize: file.size
        };

        // Upload the file and metadata
        let task = storageRef.child(name).put(file, metadata);

        let snapshot = await task;

        url = await snapshot.ref.getDownloadURL();
    }

    return url;
}
