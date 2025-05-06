import * as ScreenOrientation from 'expo-screen-orientation';
import { useEffect, useRef } from "react";
import { PanResponder, Text, View } from "react-native";

// socket logic 
let socket: WebSocket | null = null

let connectSocket = (host: string = "ws://192.168.29.221:8080/ws") => {

  if (socket) return;

  socket = new WebSocket(host)

  socket.onopen = () => {
    console.log("Websocket connection established");
  }

  socket.onclose = () => {
    console.log("Websocket connection closed");
  }

  socket.onerror = (e) => {
    console.error("An error occured", e.message);
  }

}

// sending mouspad actions 
const sendMovement = (dx: number, dy: number) => {
  if (socket?.readyState === WebSocket.OPEN) {
    socket.send(JSON.stringify({ type: "move", dx, dy }))
  }
}

const sendTap = () => {
  if (socket?.readyState === WebSocket.OPEN) {
    socket.send(JSON.stringify({ type: "click", button: "left" }))
  }
}

export default function Index() {

  useEffect(() => {
    ScreenOrientation.lockAsync(ScreenOrientation.OrientationLock.LANDSCAPE);
  }, []);

  useEffect(() => {
    connectSocket("ws://192.168.29.221:8080/ws");

  }, []);

  // panResponder 
  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onPanResponderMove: (_, gestureState) => {
        sendMovement(gestureState.dx, gestureState.dy);
      },
      onPanResponderRelease: (_, gestureState) => {
        if (Math.abs(gestureState.dx) < 5 && Math.abs(gestureState.dy) < 5) {
          sendTap();
        }
      },
    }),
  ).current;


  return (
    <View className="bg-gray-700 flex-1 justify-center items-center" {...panResponder.panHandlers}>
      <Text className="text-white text-2xl">Touch here to move cursor</Text>
    </View>
  );
}
