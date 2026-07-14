import axiosInstance from "./axiosConfig";

const AUTH_URL = "/test";

const testService = {
  test: async () => {
    try {
      const response = await axiosInstance.get(`${AUTH_URL}`);
      console.log(response);

      if (response.status === 200) {
        return response.data;
      }
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          "Erreur du serveur. Réessayez plus tard."
      );
    }
  },
};

export default testService;
