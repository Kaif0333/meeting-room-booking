import { render, screen, waitFor } from "@testing-library/react";
import App from "./App";
import { getMe } from "./api";

jest.mock("./api");

beforeEach(() => {
  getMe.mockRejectedValue(new Error("Unauthenticated"));
});

test("renders auth screen when unauthenticated", async () => {
  render(<App />);

  await waitFor(() => {
    expect(screen.getByText(/meeting room platform/i)).toBeInTheDocument();
    expect(screen.getAllByRole("button", { name: /login/i }).length).toBeGreaterThan(0);
  });
});
