"""Run after compiling Main and models into the supplied class directory."""
import subprocess
import sys
import unittest

CLASSES = sys.argv.pop(1) if len(sys.argv) > 1 else '/workspace/.cloud-setup/oop-26/classes'


def run_menu(data):
    return subprocess.run(['java', '-cp', CLASSES, 'Main'], input=data,
                          text=True, capture_output=True, timeout=5, check=True).stdout


class ProductMenuTest(unittest.TestCase):
    def test_filters(self):
        for option, included, excluded in [
            (1, ['Giay chay bo', 'Giay bong da'], ['Vot cau long', 'Bong da']),
            (2, ['Vot cau long', 'Bong da'], ['Giay chay bo', 'Giay bong da']),
            (3, ['Giay chay bo', 'Giay bong da', 'Vot cau long', 'Bong da'], []),
        ]:
            with self.subTest(option=option):
                output = run_menu(f'1\n{option}\n0\n0\n')
                for name in included:
                    self.assertEqual(output.count('Ten san pham:' + name + '\n'), 1)
                for name in excluded:
                    self.assertNotIn('Ten san pham:' + name + '\n', output)
                self.assertIn('Thoat chuong trinh!', output)

    def test_invalid_input_and_return(self):
        output = run_menu('1\nx\n99\n0\n3\n0\n')
        self.assertIn('Vui long nhap mot so!', output)
        self.assertIn('Lua chon khong hop le!', output)
        self.assertIn('DANH SACH KHACH HANG', output)
        self.assertNotIn('Ten san pham:', output)

    def test_eof(self):
        run_menu('1\n')


if __name__ == '__main__':
    unittest.main()
