package ar.edu.itba.paw.models;

public class Image {
        private long image_id;
        private byte[] imageBlob;

        public Image(long image_id, byte[] imageBlob) {
            this.image_id = image_id;
            this.imageBlob = imageBlob;
        }

        public long getImageId() {
            return image_id;
        }

        public void setImageId(long image_id) {
            this.image_id = image_id;
        }

        public byte[] getImageBlob() {
            return imageBlob;
        }

        public void setImageBlob(byte[] imageBlob) {
            this.imageBlob = imageBlob;
        }
}


